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

                <!-- Název vlaku -->
                <div class="train">
                    <img src="@/assets/train.png">
                    <div>{{ train.name }}</div>
                </div>

                <!-- Počet vagónů -->
                <div class="wagon">
                    <img src="@/assets/wagon.png">
                    <div>{{ train.wagonsCount }}</div>
                </div>

                <!-- Služby vlaku -->
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
import { TimeFormat } from "@/functions/timeFormat";
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
            
            // Informace o vlaku
            train: {} as Train,

            // Informace o trase
            routeDetail: [] as Array<Route>,
        }
    },

// Mounted ////////////////////////////////////////////////////////////////////////

    mounted() {

        // Získání detailu o trase
        axios.get("http://localhost:8080/api/routesInfo/detail/" + this.connectionID)
            .then((response) => {

                this.routeDetail = response.data.routes;
                this.train = response.data.train;

            }).catch(error => console.log(error));
    },

// Methods ////////////////////////////////////////////////////////////////////////

    methods: {

        // Formát času
        timeFormat: TimeFormat,
    },
});
</script>

<style scoped src="@/components/detail-modal/detail-modal.css" />