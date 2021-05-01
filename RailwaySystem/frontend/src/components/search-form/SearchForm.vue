<template>

<!-- Navigační lišta -->
<NavigationBar :linkSelection="activeLink.routeSearch" />

<!-- Vyhledávací formulář -->
<form @submit.prevent="redirect" autocomplete="off">

    <div class="content">

        <!-- Mapa -->
        <div class="map">
            <img class="map" src="@/assets/czech_republic.png">
            <canvas ref="canvas" />
        </div>

        <div>
            <div class="inputContainer">

                <label for="from">Odkud:</label>

                <!-- Výběr startovního nádraží -->
                <input id="from" type="text" v-model="formInputs.from.name" 
                    @click="inputFocus.from = true" 
                    @blur="inputFocus.from = false"
                    @keyup="stationFilter(formInputs.from.name, inputElement.from)">
            </div>

            <!-- Možnosti nádraží -->
            <div v-show="inputFocus.from" class="options">

                <div v-for="station in filteredStationsFrom" :key="station.id" 
                    @mousedown="setStation(station.name, inputElement.from)">
                    
                    {{station.name}}
                </div>
            </div>
        </div>

        <div>
            <div class="inputContainer">

                <label for="to">Kam:</label>

                <!-- Výběr cílového nádraží -->
                <input id="to" type="text" v-model="formInputs.to.name" 
                    @focus="inputFocus.to = true" 
                    @blur="inputFocus.to = false" 
                    @keyup="stationFilter(formInputs.to.name, inputElement.to)">
            </div>

            <!-- Možnosti nádraží -->
            <div v-if="inputFocus.to" class="options">

                <div v-for="station in filteredStationsTo" :key="station.id"
                    @mousedown="setStation(station.name, inputElement.to)">
                    
                    {{station.name}}
                </div>
            </div>
        </div>

        <!-- Čas odjezdu -->
        <div class="departureTime">
            
            <div class="departureTimeContainer">
                <button type="button" @click="changeTime('-')">-</button>

                <label>Odjezd:</label>
                <div>{{ formInputs.departure }}</div>

                <button type="button" @click="changeTime('+')">+</button>
            </div>
        </div>

        <div>
            <button :disabled="formInputs.from.id === 0 || formInputs.to.id === 0"
                :class="(formInputs.from.id === 0 || formInputs.to.id === 0) ? 'disabled' : ''">

                Vyhledat
            </button>
        </div>

        <!-- Historie vyhledávání -->
        <div v-if="searchHistory.length != 0" class="searchHistory">

            <div class="header">Historie vyhledávání</div>

            <div v-for="(search, index) in searchHistory" :key="index">

                <div class="searchContainer" @click="redirectFromHistory(search)">

                    <div class="departure">{{ search.departure }}</div>
                    <div class="fromName">{{ search.from.name }}</div>
                    <img src="@/assets/right_arrow3.png">
                    <div class="toName">{{ search.to.name }}</div>
                </div>
            </div>
        </div>
    </div>
</form>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios';
import NavigationBar from "@/components/navigation-bar/NavigationBar.vue";
import { Station } from "@/models/station";
import { InputElement } from "@/enums/inputElement";
import MarkImage from "@/assets/mark.png";
import { ActiveLink } from "@/enums/activeLink";
import { SearchForm } from "@/models/searchForm";

export default defineComponent({
        
    name: "SearchForm",

    components: {
        NavigationBar,
    },

// Data /////////////////////////////////////////////////////////////////////////////////////////////

    data() {
        return {

            // Enum aktivní link [routeSearch, trainMap]
            activeLink: ActiveLink,

            // Enum input elementů [from, to]
            inputElement: InputElement,

            // Focus event input elementů
            inputFocus: {
                from: false,
                to: false,
            },

            // Pozice nádraží pro vykreslení na mapě
            stationsToDraw: {
                from: {
                    x: 0,
                    y: 0,
                },
                to: {
                    x: 0,
                    y: 0,
                }
            },

            // Data formuláře
            formInputs: {
                from: {
                    id: 0,
                    name: "",
                },
                to: {
                    id: 0,
                    name: "",
                },
                departure: new Date().getHours() + ":00",
            },

            // Historie vyhledávání
            searchHistory: [] as Array<SearchForm>,

            // Seznam nádraží
            stations: [] as Array<Station>,
            filteredStationsFrom: [] as Array<Station>,
            filteredStationsTo: [] as Array<Station>,
        }
    },

// Mounted ///////////////////////////////////////////////////////////////////////////////////////////

    mounted() {

        // Získání seznamu nádraží
        axios.get("http://localhost:8080/api/stations")
            .then((response) => {

            this.stations = response.data;

            this.filteredStationsFrom = this.stations;
            this.filteredStationsTo = this.stations;

        }).catch(error => console.log(error));

        // Historie vyhledávání
        const localStorageItem = localStorage.getItem("searchHistory");

        if (localStorageItem) this.searchHistory = JSON.parse(localStorageItem);
    },

// Methods ////////////////////////////////////////////////////////////////////////////////////////////
    
    methods: {


        /**
         * Změna času
         * 
         * @param mark - ['+' zvýšení / '-' snížení]
         */
        changeTime(mark: string): void {
                
            let hour: number = parseInt(this.formInputs.departure.split(":")[0]);

            // Zvýšení
            if (mark === "+") {

                if (hour < 23) {
                    hour++;
                } else hour = 0;

            // Snížení
            } else if (mark === "-") {

                if (hour > 0) {
                    hour--;
                } else hour = 23;
            }

            this.formInputs.departure = hour + ":00";
        },


        /**
         * Filtr nádraží
         * 
         * @param value - vstupní hodnota filtru
         * @param inputSelect - enum input elementů [from, to]
         */
        stationFilter(value: string, inputSelect: InputElement): void {

            switch (inputSelect) {

                case InputElement.from:
                    this.filteredStationsFrom = this.stations.filter(station => station.name.includes(value));
                    break;

                case InputElement.to:
                    this.filteredStationsTo = this.stations.filter(station => station.name.includes(value));
                    break;
            }
        },


        /**
         * Nastavení nádraží + skrytí filtru
         * 
         * @param stationName - název nádraží
         * @param inputElement - enum input elementů [from, to]
         */
        setStation(stationName: string, inputElement: InputElement): void {
                
            switch (inputElement) {

                case InputElement.from:
                    this.formInputs.from.name = stationName;
                    this.inputFocus.from = false;
                    break;

                case InputElement.to:
                    this.formInputs.to.name = stationName;
                    this.inputFocus.to = false;
                    break;
            }
        },


        /**
         * Vykreslení pozic nádraží na mapě
         */
        drawCityMarks(): void {

            const canvas = this.$refs.canvas as HTMLCanvasElement;
            const context = canvas.getContext("2d") as CanvasRenderingContext2D;

            canvas.height = 400;
            canvas.width = 600;

            // Obrázek značky nádraží
            const markImage = new Image();
            markImage.src = MarkImage;

            markImage.onload = () => {

                // Vykreslení startovního nádraží na mapě
                if (this.stationsToDraw.from.x !== 0 && this.stationsToDraw.from.y !== 0) {

                    context.drawImage(markImage, this.stationsToDraw.from.x, 
                        this.stationsToDraw.from.y, 20, 20);
                }
                
                // vykreslení cílového nádraží na mapě
                if (this.stationsToDraw.to.x !== 0 && this.stationsToDraw.to.y !== 0) {

                    context.drawImage(markImage, this.stationsToDraw.to.x, 
                        this.stationsToDraw.to.y, 20, 20);
                }
            };
        },


        /**
         * Přesměrování na výsledky vyhledávání z historie
         */
        redirectFromHistory(searchForm: SearchForm) {

            this.formInputs = searchForm;

            // Přesměrování na výsledky vyhledávání
            this.redirect();
        },


        /**
         *  Přesměrování na výsledky vyhledávání
         */
        redirect(): void {

            // Data z vyhledávacího formuláře
            const searchForm: SearchForm = {
                from: {
                    id: this.formInputs.from.id,
                    name: this.formInputs.from.name,
                },
                to: {
                    id: this.formInputs.to.id,
                    name: this.formInputs.to.name,
                },
                departure: this.formInputs.departure,
            }

            // Vyhledání indexu se stejnými parametry
            const searchIndex = this.searchHistory.findIndex(item => 
                item.from.id === searchForm.from.id &&
                item.to.id === searchForm.to.id &&
                item.departure === searchForm.departure
            );

            // Vymazání předešlého záznamu
            if (searchIndex !== -1) this.searchHistory.splice(searchIndex, 1);

            // Přidání záznamu na začátek historie
            this.searchHistory.unshift(searchForm);

            // Uložení 5 záznamů historie vyhledávání
            localStorage.setItem("searchHistory", JSON.stringify(this.searchHistory.splice(0, 5)));

            this.$router.push({

                name: "RoutesInfo",
                path: "/vysledky",
                query: {
                    fromId: this.formInputs.from.id,
                    toId: this.formInputs.to.id,
                    departure: this.formInputs.departure,
                }
            });
        }
    },

// Watch /////////////////////////////////////////////////////////////////////////////////////////////

    watch: {

        /**
         * Hlídání vstupu [Odkud]
         */
        'formInputs.from.name': function(inputValue: string): void {

            let isInputValueStationName = false;

            for (const station of this.filteredStationsFrom) {

                if (inputValue === station.name) {

                    isInputValueStationName = true;

                    // ID nádraží
                    this.formInputs.from.id = station.id

                    // Pozice nádraží
                    this.stationsToDraw.from.x = station.positionX;
                    this.stationsToDraw.from.y = station.positionY;
                }
            }

            // Vynulování pozice nádraží + id, při neodpovídajícímu názvu
            if (!isInputValueStationName) {

                this.stationsToDraw.from.x = 0;
                this.stationsToDraw.from.y = 0;

                this.formInputs.from.id = 0;
            }

            // Vykreslení pozic nádraží na mapě
            this.drawCityMarks();
        },


        /**
         * Hlídání vstupu [Kam]
         */
        'formInputs.to.name': function(inputValue: string): void {
            
            let isInputValueStationName = false;

            for (const station of this.filteredStationsTo) {

                if (inputValue === station.name) {

                    isInputValueStationName = true;

                    // ID nádraží
                    this.formInputs.to.id = station.id

                    // Pozice nádraží
                    this.stationsToDraw.to.x = station.positionX;
                    this.stationsToDraw.to.y = station.positionY;
                }
            }

            // Vynulování pozice nádraží + id, při neodpovídajícímu názvu
            if (!isInputValueStationName) {

                this.stationsToDraw.to.x = 0;
                this.stationsToDraw.to.y = 0;

                this.formInputs.to.id = 0;
            }

            // Vykreslení pozic nádraží na mapě
            this.drawCityMarks();
        }
    }
});
</script>

<style scoped src="@/components/search-form/search-form.css" />