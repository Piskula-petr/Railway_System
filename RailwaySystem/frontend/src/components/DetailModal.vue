<template>
<div class="background">
    
    <div class="container">

        <!-- Záhlaví -->
        <div class="header">Detail spoje
            <img @click="$emit('closeModal')" src="@/assets/close.png">
        </div>

        <!-- Obsah -->
        <div class="content">

            <!-- Trasa -->
            <div class="route">
                <template v-for="(route, index) in routeDetail" :key="route.id">

                    <!-- Logo trasy -->
                    <div class="routeLogo">
                        <div v-if="index === 0">
                            <img src="@/assets/route_start.png">
                        </div>

                        <div v-else-if="index === routeDetail.length - 1">
                            <img src="@/assets/route_end.png">
                        </div>

                        <div v-else>
                            <img src="@/assets/route_middle.png">
                        </div>
                    </div>

                    <!-- Čas odjezdu / příjezdu -->
                    <div class="time">{{ timeFormat((index === 0) ? route.departure : route.arrival) }}</div>

                    <!-- Název nástupní nádraží -->
                    <div class="station">{{ route.stationName.name }}</div>

                    <!-- Vzdálenost -->
                    <div class="distance">{{ route.distance }} km</div>
                </template>
            </div>

            <!-- Informace o vlaku -->
            <div class="trainInfo">

                <div class="train">
                    <img src="@/assets/train.png">
                    <div>{{ train.name }}</div>
                </div>

                <div class="wagon">
                    <img src="@/assets/wagon.png">
                    <div>{{ train.wagonsCount }}</div>
                </div>

                <div class="trainService">
                    <img v-show="train.wifi" src="@/assets/wifi.png">
                    <img v-show="train.restaurant" src="@/assets/restaurant.png">
                    <img v-show="train.handicap" src="@/assets/handicap.png">
                    <img v-show="train.bike" src="@/assets/bike.png">
                </div>
            </div>
        </div>      
    </div>
</div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import axios from 'axios';
import { TimeFormat } from "../functions/timeFormat";
import { Train } from "@/models/train";
import { Route } from "@/models/route";

export default defineComponent({

    name: "DetailModal",

// Emits //////////////////////////////////////////////////////////////////////////

    emits: ["closeModal"],

// Props //////////////////////////////////////////////////////////////////////////

    props: ["connectionID"],

// Data ////////////////////////////////////////////////////////////////////////////

    data() {
        return {
            
            train: {} as Train,
            routeDetail: [] as Array<Route>,
        }
    },

// Mounted ////////////////////////////////////////////////////////////////////////

    mounted() {

        axios.get("http://localhost:8080/api/routesInfo/detail/" + this.connectionID)
            .then((response) => {

                this.routeDetail = response.data.routes;
                this.train = response.data.train;

            }).catch(error => console.log(error));
    },

// Methods ////////////////////////////////////////////////////////////////////////

    methods: {

        timeFormat: TimeFormat,
    },
});
</script>

<style scoped>
.background {
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background: rgba(0,0,0,0.3);
}

.container {
    width: 900px;
    background: #EEE;
    border: 0px solid #c2c2c2;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
}

.header {
    text-align: left;
}

.header img {
    float: right;
    margin: 0px -5px 0px 0px;
    height: 20px;
}

.header img:hover {
    cursor: pointer;
    opacity: 0.8;
}

.content {
    display: flex;
    flex-direction: row;
    width: 100%;
    padding: 20px 0px;
}

.route {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    align-items: center;
    flex-grow: 1;
    font-size: 1.2em;
    flex-basis: 50%;
    min-height: 150px;
    margin: auto 0px;
    padding: 0px 0px 0px 20px;
}

.route .routeLogo {
    flex-basis: 40px;
}

.route .routeLogo img {
    display: block;
}

.route .time {
    flex-basis: 70px;
}

.route .station {
    padding: 0px 0px 0px 25px;
    text-align: left;
    flex-grow: 1;
    flex-basis: 50%;
}

.route .distance {
    padding: 0px 20px 0px 0px;
    text-align: right;
    flex-basis: 90px;
}

/*__________________________ Informace o vlaku __________________________*/
.trainInfo {
    width: 310px;
    font-size: 1.2em;
    border-left: 2px solid #c2c2c2;
}

.trainInfo img {
    width: 100px;
}

.train, .wagon {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 20px 0px;
}

.train div, .wagon div {
    padding: 10px;
}

.trainService {
    display: flex;
    justify-content: center;
}

.trainService img {
    width: 25px;
    padding: 10px;
    margin: 0px 12px;
    background: #41B883;
    border-radius: 5px;
}
</style>