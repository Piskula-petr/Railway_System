/* eslint-disable no-case-declarations */
<template>

<!-- Navigační lišta -->
<NavigationBar :linkSelection="activeLink.routeSearch" />

<div class="content">

    <div class="routesContainer">

        <!-- Načtení dřívějších spojů -->
        <button v-if="!isFirstConnectionIdAgain" class="loadButton" @click="getMoreRoutesInfo(loadOption.previous)">
            Načíst dřívější
        </button>

        <div v-for="routeInfo in routesInfo" :key="routeInfo.connection.id">

            <!-- Trasa -->
            <div class="routeContainer">

                <div class="header">

                    <!-- Název vlaku -->
                    <div class="train">{{ routeInfo.connection.trainName.name }}</div>

                    <!-- Název společnosti -->
                    <div class="company">{{ routeInfo.connection.company.name }}</div>
                </div>

                <!-- Obsah trasy -->
                <div class="routeContent">

                    <!-- Logo trasy -->
                    <div class="routeLogo">
                        <img src="@/assets/route.png">
                    </div>

                    <!-- čas příjezdu / odjezdu -->
                    <div class="arrivalDeparture">

                        <div>{{ timeFormat(routeInfo.startRoute.departure) }}</div>
                        <div>{{ timeFormat(routeInfo.endRoute.arrival) }}</div>
                    </div>

                    <!-- Název nástupního / výstupního nádraží -->
                    <div class="station">

                        <div>{{ routeInfo.startRoute.stationName.name }}</div>
                        <div>{{ routeInfo.endRoute.stationName.name }}</div>
                    </div>

                    <!-- Informace o trase -->
                    <div class="routeInfo">

                        <!-- Celkový čas -->
                        <div class="routeTime">

                            <img src="@/assets/time.png">

                            <div>{{ timeDifference(routeInfo.startRoute.departure, 
                                routeInfo.endRoute.arrival) }}</div>
                        </div>

                        <!-- Vzdálenost -->
                        <div class="distance">

                            <img src="@/assets/distance.png">

                            <div>{{ routeInfo.endRoute.distance - routeInfo.startRoute.distance }} km</div>
                        </div>
                    </div>         

                    <!-- Cena / Detail -->
                    <div class="priceAndDetail">

                        <!-- Cena -->
                        <div class="price">
                            Cena {{ getPrice(routeInfo.endRoute.distance - routeInfo.startRoute.distance, 
                                routeInfo.connection.company) }} Kč
                        </div>

                        <button class="detail" @click="isModalClosed = !isModalClosed; 
                            connectionID = routeInfo.connection.id">
                            
                            Detail
                        </button>
                    </div>
                </div>
            </div>
        </div>   

        <!-- Načtení pozdějších spojů -->
        <button v-if="!isLastConnectionIdAgain" class="loadButton" @click="getMoreRoutesInfo(loadOption.later)">
            Načíst pozdější
        </button> 
    </div>

    <!-- Výběr možností -->
    <div class="optionsContainer">

        <!-- Typ cestujícího -->
        <div class="passengerTypeContainer">

            <div class="header">Typ cestujícího</div>

            <div class="passengerSelect">

                <input type="radio" name="passengerType" id="child" @click="setPassengerType(passengerType.child)">
                <label for="child">Dítě</label> <br>

                <input type="radio" name="passengerType" id="student" @click="setPassengerType(passengerType.student)">
                <label for="student">Student</label> <br>

                <input type="radio" checked name="passengerType" id="adult" @click="setPassengerType(passengerType.adult)">
                <label for="adult">Dospělý</label> <br>

                <input type="radio" name="passengerType" id="senior" @click="setPassengerType(passengerType.senior)">
                <label for="senior">Senior</label>
            </div>
        </div>

        <!-- Výběr třídy -->
        <div class="classSelectContainer">

            <div class="header">Výběr třídy</div>

            <div class="classSelect">

                <input type="radio" name="classType" id="firstClass" @click="setClassType(classType.firstClass)">
                <label for="firstClass">První třída</label> <br>

                <input type="radio" checked name="classType" id="secondClass" @click="setClassType(classType.secondClass)">
                <label for="secondClass">Druhá třída</label>
            </div>
        </div>
    </div>

    <!-- Modal detailu spoje -->
    <DetailModal v-if="!isModalClosed" :connectionID="connectionID" 
        @closeModal="isModalClosed = true">

    </DetailModal>
</div>
</template>

<script lang="ts">
import { defineComponent } from "vue"
import axios from 'axios';
import moment from "moment";
import { RouteInfo } from "@/models/routeInfo";
import { Company } from "@/models/company";
import { PassengerType } from "@/enums/passengerType";
import { ClassType } from "@/enums/classType";
import { TimeFormat } from "@/functions/timeFormat";
import DetailModal from "@/components/detail-modal/DetailModal.vue";
import NavigationBar from "@/components/navigation-bar/NavigationBar.vue";
import { ActiveLink } from "@/enums/activeLink";
import { LoadOption } from "@/enums/loadOption";

export default defineComponent({

    name: "RoutesInfo",

    components: {
        DetailModal,
        NavigationBar,
    },

// Data //////////////////////////////////////////////////////////////////////////////////////////////

    data() {
        return{

            // ID spoje
            connectionID: 0,

            // Zobrazení modalu
            isModalClosed: true,

            // První / poslední ID spoje
            firstConnectionId: 0,
            lastConnectionId: 0,

            // Znovu načtení prvního / posledního spoje
            isFirstConnectionIdAgain: false,
            isLastConnectionIdAgain: false,

            // Aktuální typ cestujícího - default adult
            currentPassengerType: PassengerType.adult,

            // Aktuální typ třídy - default secondClass
            currentClassType: ClassType.secondClass,

            // Enum typ cestujícího [child, student, adult, senior]
            passengerType: PassengerType,

            // Enum typ třídy [firstClass, secondClass]
            classType: ClassType,

            // Enum aktivní link [routeSearch, trainMap]
            activeLink: ActiveLink,

            // Enum volba načítání [previous, next]
            loadOption: LoadOption,

            // Informace o spojích
            routesInfo: [] as Array<RouteInfo>,
        }
    },  

// Mounted ////////////////////////////////////////////////////////////////////////////////////////////

    mounted() {

        const fromId = this.$route.query.fromId;
        const toId = this.$route.query.toId;
        const departure = this.$route.query.departure;

        // Získání informací o trasách
        axios.get("http://localhost:8080/api/routesInfo/fromId=" + fromId + 
            "&toId=" + toId + "&departure=" + departure +
            "&loadOption=" + LoadOption[this.loadOption.later])

            .then((response) => {

                this.routesInfo = response.data;

                // Nastavení ID prvního spoje
                this.firstConnectionId = this.routesInfo[0].connection.id;

                // Nastavení ID posledního spoje
                this.lastConnectionId = this.routesInfo[this.routesInfo.length - 1].connection.id;

        }).catch(error => console.log(error));
    },

// Methods ////////////////////////////////////////////////////////////////////////////////////////////

    methods: {


        // Formát času
        timeFormat: TimeFormat,


        /**
         * Rozdíl mezi dvěma časy
         * 
         * @param start - počáteční čas
         * @param end - koncový čas
         */
        timeDifference(start: string, end: string): string {

            const startTime = moment(start, "HH:mm:ss");
            const endTime = moment(end, "HH:mm:ss");

            const difference = endTime.diff(startTime);

            return moment.utc(difference).hour() + " hod " + moment.utc(difference).minute() + " min";
        },


        /**
         * Nastavení typu cestujícího
         * 
         * @param passengerType - Enum typ cestujícího [child, student, adult, senior]
         */
        setPassengerType(passengerType: PassengerType): void {

            this.currentPassengerType = passengerType;
        },


        /**
         * Nastavení typu třídy
         * 
         * @param classType - Enum typ třídy [firstClass, secondClass]
         */
        setClassType(classType: ClassType):void {

            this.currentClassType = classType;
        },


        /**
         * Výpočet ceny
         * 
         * @param distance - vzdálenost
         * @param company - interface Company
         */
        getPrice(distance: number, company: Company): string {

            // Příplatek za zvolenou třídu
            const classCharge: number = (this.currentClassType === ClassType.firstClass 
                ? company.classCharge : 1);

            switch (this.currentPassengerType) {

                case PassengerType.child:   
                    return ((company.childTariff * distance) * classCharge).toFixed(0);

                case PassengerType.student:
                    return ((company.studentTariff * distance) * classCharge).toFixed(0);

                case PassengerType.adult:
                    return ((company.adultTariff * distance) * classCharge).toFixed(0);

                case PassengerType.senior: 
                    return ((company.seniorTariff * distance) * classCharge).toFixed(0);
            }
        },


        /**
         * Načtení více spojů
         * 
         * @param loadOption - Enum volba načítání [previous, next]
         */
        getMoreRoutesInfo(loadOption: LoadOption) {

            const fromId = this.$route.query.fromId;
            const toId = this.$route.query.toId;

            let departure: string;

            switch (loadOption) {

                case LoadOption.previous:
                    departure = this.routesInfo[0].startRoute.departure; 
                    break;


                case LoadOption.later:
                    departure = this.routesInfo[this.routesInfo.length - 1].startRoute.departure; 
                    break;    
            }

            // Získání informací o trasách
            axios.get("http://localhost:8080/api/routesInfo/fromId=" + fromId + 
                "&toId=" + toId + "&departure=" + this.timeFormat(departure) + 
                "&loadOption=" + LoadOption[loadOption])

                .then((response) => {

                    const routesInfoResponse: Array<RouteInfo> = response.data;

                    switch (loadOption) {

                        case LoadOption.previous:

                            this.routesInfo = routesInfoResponse.concat(this.routesInfo);

                            // Skrytí tlačítka pro načtení dřívějších spojů
                            if (routesInfoResponse.find(routeInfo => routeInfo.connection.id == this.firstConnectionId)) {
                                
                                this.isFirstConnectionIdAgain = true;
                            }

                            break;

                        case LoadOption.later:

                            this.routesInfo = this.routesInfo.concat(routesInfoResponse);

                            // Skrytí tlačítka pro načtení pozdějších spojů
                            if (routesInfoResponse.find(routeInfo => routeInfo.connection.id == this.lastConnectionId)) {

                                this.isLastConnectionIdAgain = true;
                            }

                            break;
                    }

            }).catch(error => console.log(error));
        }
    },
})
</script>

<style scoped src="@/components/route-info/route-info.css" />