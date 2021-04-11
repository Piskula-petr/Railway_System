<template>

<NavigationBar />

<div class="routeOverview">
    <h2>{{ startStation }}</h2>
    <h2>>></h2>
    <h2>{{ endStation }}</h2>
</div>

<div class="routesContainer">

    <div>
        <div v-for="routeInfo in routesInfo" :key="routeInfo.connection.id">

            <!-- Spoj -->
            <div class="routeContainer">
                <div class="header">

                    <!-- Název vlaku -->
                    <div class="train">{{ routeInfo.connection.trainName.name }}</div>

                    <!-- Název společnosti -->
                    <div class="company">{{ routeInfo.connection.company.name }}</div>
                </div>

                <div class="tableContainer">
                    <table>
                        <tr>
                            <td rowspan="2">
                                <img class="routeLogo" src="@/assets/route.png">
                            </td>

                            <td>
                                <!-- Čas odjezdu -->
                                <div>{{ timeFormat(routeInfo.startRoute.departure) }}</div>
                            </td>

                            <td>
                                <!-- Název nástupní nádraží -->
                                <div>{{ routeInfo.startRoute.stationName.name }}</div>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <!-- Čas pžíjezdu -->
                                <div>{{ timeFormat(routeInfo.endRoute.arrival) }}</div>
                            </td>

                            <td>
                                <!-- Název výstupního nádraží -->
                                <div>{{ routeInfo.endRoute.stationName.name }}</div>
                            </td>
                        </tr>
                    </table>

                    <table>
                        <tr>
                            <td>
                                <img src="@/assets/time.png">
                            </td>

                            <td>
                                <!-- Celkový čas -->
                                <div>{{ timeDifference(routeInfo.startRoute.departure, 
                                    routeInfo.endRoute.arrival) }}</div>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <img src="@/assets/distance.png">
                            </td>

                            <td>
                                <!-- Vzdálenost -->
                                <div>{{ routeInfo.endRoute.distance - routeInfo.startRoute.distance }} km</div>
                            </td>
                        </tr>
                    </table>

                    <table>
                        <tr>
                            <td>
                                <!-- Cena -->
                                <div class="price">
                                    Cena {{ getPrice(routeInfo.endRoute.distance - routeInfo.startRoute.distance, 
                                        routeInfo.connection.company) }} Kč
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <button class="detail" @click="isModalClosed = !isModalClosed; 
                                    connectionID = routeInfo.connection.id">Detail
                                </button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>    
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
import { RouteInfo } from "../models/routeInfo";
import { Company } from "../models/company";
import { PassengerType } from "../enums/passengerType";
import { ClassType } from "../enums/classType";
import DetailModal from "../components/DetailModal.vue";
import NavigationBar from "../components/NavigationBar.vue";
import { TimeFormat } from "../functions/timeFormat";

export default defineComponent({

    name: "RoutesInfo",

    components: {
        DetailModal,
        NavigationBar,
    },

// Data //////////////////////////////////////////////////////////////////////////////////////////////

    data() {
        return{

            startStation: "",
            endStation: "",

            // ID spoje
            connectionID: 0,

            // Zobrazení modalu
            isModalClosed: true,

            // Aktuální typ cestujícího - default adult
            currentPassengerType: PassengerType.adult,

            // Aktuální typ třídy - default secondClass
            currentClassType: ClassType.secondClass,

            // Enum typ cestujícího [child, student, adult, senior]
            passengerType: PassengerType,

            // Enum typ třídy [firstClass, secondClass]
            classType: ClassType,

            // Informace o spojích
            routesInfo: [] as Array<RouteInfo>,
        }
    },  

// Mounted ////////////////////////////////////////////////////////////////////////////////////////////

    mounted() {

        const fromId = this.$route.query.fromId;
        const toId = this.$route.query.toId;
        const departure = this.$route.query.departure;

        // Získání informací o spojích
        axios.get("http://localhost:8080/api/routesInfo/fromId=" + fromId + 
            "&toId=" + toId + "&departure=" + departure)

            .then((response) => {

                this.routesInfo = response.data;

                this.startStation = response.data[0].startRoute.stationName.name;
                this.endStation = response.data[0].endRoute.stationName.name;

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
        }
    },
})
</script>

<style scoped>
.routeOverview {
    width: 1200px;
    display: flex;
    justify-content: space-around;
    margin: 5px auto;
    background: #EEE;
    border: 2px solid #c2c2c2;
    border-radius: 5px;
}

.routesContainer {
    width: 1200px;
    display: flex;
    text-align: center;
    margin: auto;
    background: #EEE;
    border: 2px solid #c2c2c2;
    border-radius: 5px;
}

.routeContainer {
    background: white;
    min-height: 100px;
    width: 900px;
    margin: 60px 20px;
}

.header .train {
    float: left;
}

.company {
    text-align: right;
}

.routeLogo {
    height: 75px;
    margin: 3px 0px 0px 0px;
    padding: 20px 0px;
}

/*__________________________ Table __________________________*/
.tableContainer {
    display: flex;
}

table {
    font-size: 1.2em;
    padding: 10px 15px;
    margin: 20px;
}

table:first-of-type {
    width: 40%;
}

table:last-of-type {
    margin-left: auto;
    border-left: 2px solid #c2c2c2;
}

table tr td {
    padding: 0px 10px;
    text-align: left;
    height: 58px;
}

img {
    height: 25px;
}

.price {
    text-align: right;
    margin: 0px 0px 10px 30px;
}

.detail {
    float: right;
    font-size: 1.1em;
    color: #EEE;
    padding: 7px 20px;
    margin: 10px 0px 0px 70px;
    background: #41B883;
    border: none;
    border-radius: 5px;
}

.detail:focus {
    outline: none;
}

.detail:hover {
    cursor: pointer;
    opacity: 0.9;
}

/*__________________________ Výběr možností __________________________*/
.optionsContainer {
    display: flex;
    flex-direction: column;
    margin: 0px 0px 20px 0px;
}

.passengerTypeContainer, .classSelectContainer{
    text-align: left;
    width: 220px;
    background: white;
    margin: 60px 0px 0px 20px;
}

.passengerSelect, .classSelect {
    padding: 20px;
}

.optionsContainer input[type = 'radio'] {
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;

    display: inline-block;
    width: 1.7em;
    height: 1.7em;
    padding: 4px;

    background-clip: content-box;
    border: 1px solid #bbbbbb;
    background-color: #e7e6e7;
    border-radius: 50%;

    margin: 5px 10px 5px 5px;
}

.optionsContainer input[type = "radio"]:checked {
    background-color: #41B883;
    border: 1px solid #41B883;
}

.optionsContainer input[type = "radio"]:focus {
    outline: none;
}

.optionsContainer input[type = "radio"]:hover, .optionsContainer label {
    opacity: 0.8;
    cursor: pointer;
}

.optionsContainer label {
    position: absolute;
    margin: 7px 0px 0px 0px;
    font-size: 1.2em;
}

</style>