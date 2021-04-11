<template>

<NavigationBar />

<form @submit.prevent="onSubmit" autocomplete="off">

    <table>

        <tr>
            <td>
                <!-- Mapa -->
                <img class="map" src="@/assets/czech_republic.png">
                <canvas ref="canvas" />
            </td>
        </tr>

        <tr>
            <td>
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
            </td>
        </tr>

        <tr>
            <td>
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
            </td>
        </tr>

        <tr>
            <td>
                <!-- Čas odjezdu -->
                <div class="timeContainer">
                    <button type="button" @click="changeTime('-')">-</button>

                    <label>Odjezd:</label>
                    <div>{{ formInputs.departure }}</div>

                    <button type="button" @click="changeTime('+')">+</button>
                </div>
            </td>
        </tr>

        <tr>
            <td>
                <button :disabled="formInputs.from.id === 0 || formInputs.to.id === 0"
                    :class="(formInputs.from.id === 0 || formInputs.to.id === 0) ? 'disabled' : ''">
                    Vyhledat
                </button>
            </td>
        </tr>
    </table>
</form>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import NavigationBar from "./NavigationBar.vue";
import { Station } from "../models/station";
import { InputElement } from "../enums/inputElement";
import MarkImage from "../assets/mark.png";
import axios from 'axios';

export default defineComponent({
        
    name: "SearchForm",

    components: {
        NavigationBar,
    },

// Data /////////////////////////////////////////////////////////////////////////////////////////////

    data() {
        return {

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

            switch (InputElement[inputSelect]) {

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
                
            switch (InputElement[inputElement]) {

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

                if (this.stationsToDraw.from.x !== 0 && this.stationsToDraw.from.y !== 0) {

                    context.drawImage(markImage, this.stationsToDraw.from.x, 
                        this.stationsToDraw.from.y, 20, 20);
                }
                
                if (this.stationsToDraw.to.x !== 0 && this.stationsToDraw.to.y !== 0) {

                    context.drawImage(markImage, this.stationsToDraw.to.x, 
                        this.stationsToDraw.to.y, 20, 20);
                }
            };
        },


        /**
         *  Přesměrování po odeslání formuláře
         */
        onSubmit(): void {

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

<style scoped>
form {
    width: 1200px;
    margin: auto;
    background: #EEE;
    border: 2px solid #c2c2c2;
    border-radius: 5px;
}

table {
    margin: 60px auto;
}

.map {
    position: absolute;
    margin: 0px 0px 0px 40px;
    width: 525px;
    height: 350px;
}

canvas {
    display: block;
    background: transparent;
    width: 525px;
    height: 350px;
    margin: 0 auto;
}

label {
    display: inline-block;  
    color: #808080;
    font-size: 1.2em;
    width: 80px;
    padding: 10px 0px 10px 10px;
    background: #EEE;
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
}

input {
    display: inline-block; 
    width: 500px;
    font-size: 1.2em;
    padding: 10px 0px 10px 10px;
    outline: none;
    border: none;
    border-top-right-radius: 5px;
    border-bottom-right-radius: 5px;
}

.inputContainer {
    border: 2px solid #41B883;
    border-radius: 5px;
    margin: 50px 0px 0px 0px;
}

/*__________________________ Možnosti nádraží __________________________*/

.options {
    position: absolute;
    z-index: 1;
    margin: -1px 0px 0px 0px;
    color: #808080;
    background: #EEE;
    width: 602px;
    border: 1px solid #41B883;
    font-size: 1.2em;   
    border-radius: 5px;
}

.options div {
    padding: 2px 5px;
}

.options div:hover {
    cursor: pointer;
    background: whitesmoke;
}

.options div:hover:first-child {
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
}

.options div:hover:last-child {
    border-bottom-left-radius: 5px;
    border-bottom-right-radius: 5px;
}

/*__________________________ Čas odjezdu __________________________*/

.timeContainer {
    display: inline-block; 
    margin: 50px 0px 0px 0px;
    border: 2px solid #41B883;
    border-radius: 5px;
}

.timeContainer button {
    margin: 0;
    width: 50px;
    border-radius: 0;
    background: #41B883;
}

.timeContainer button:hover {
    opacity: 0.9;
}

.timeContainer div {
    display: inline-block; 
    text-align: center;
    color: #808080;
    font-size: 1.2em;
    width: 80px;
    padding: 10px;
    background: white;
}

/*__________________________ Tlačítko __________________________*/

button {
    color: #EEE;
    font-size: 1.2em;
    width: 604px;
    margin: 60px 0px 0px 0px;
    padding: 10px 20px;
    background: #41B883;
    border-radius: 5px;
    border: none;
}

button:hover {
    cursor: pointer;
    opacity: 0.9;
}

button:focus {
    outline: none;
}

.disabled {
    background: #c2c2c2;
}
</style>