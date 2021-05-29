<template>

<!-- Navigační lišta -->
<NavigationBar :linkSelection="activeLink.trainMap" />

<div class="container">

    <!-- Mapa -->
    <div class="map">

        <img src="@/assets/czech_republic_rails.png">
        
        <canvas ref="trainCanvas" />
        <canvas ref="labelCanvas" />

        <!-- Legenda mapy -->
        <div class="mapLegend">
            <div>
                <img src="@/assets/train_mark_ceske_drahy.png">
                {{ companies.ceskeDrahy }}
            </div>

            <div>
                <img src="@/assets/train_mark_regiojet.png">
                {{ companies.regioJet }}
            </div>

            <div>
                <img src="@/assets/train_mark_leo_express.png">
                {{ companies.leoExpress }}
            </div>
        </div>
    </div>

    <!-- Informace o trasách vlaků -->
    <div class="information">

        <div v-for="(connectionName, index) in connectionNames" :key="connectionName.id">

            <div @mouseenter="drawTrainName(checkpoints[index], connectionName.trainName.name)"
                     @mouseleave="clearTrainName()">
                
                <div class="header">{{ connectionName.trainName.name }}</div>

                <div class="trainInformation">

                    <!-- Začátek trasy -->
                    <div class="startRoute">
                        <div>{{ timeFormat((startRoutes[index].arrival != null 
                            ? startRoutes[index].arrival 
                                : startRoutes[index].departure)) }}</div>

                        <div class="stationName">{{ startRoutes[index].stationName.name }}</div>
                    </div>

                    <!-- Konec trasy -->
                    <div class="endRoute"> 
                        <div>{{ timeFormat(endRoutes[index].arrival) }}</div>
                        <div class="stationName">{{ endRoutes[index].stationName.name }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

</template>

<script lang="ts">
import { defineComponent } from 'vue'
import axios from 'axios';
import { VueCookieNext } from 'vue-cookie-next'
import NavigationBar from "@/components/navigation-bar/NavigationBar.vue";
import { ActiveLink } from "@/enums/activeLink";
import { Checkpoint } from "@/models/checkpoint";
import { ConnectionName } from "@/models/connectionName";
import { Route } from '@/models/route';
import { Companies } from "@/enums/companies";
import { TimeFormat } from "@/functions/timeFormat";
import TrainMarkCeskeDrahy from "@/assets/train_mark_ceske_drahy.png";
import TrainMarkLeoExpress from "@/assets/train_mark_leo_express.png";
import TrainMarkRegioJet from "@/assets/train_mark_regiojet.png";

export default defineComponent({

    name: "TrainMap",

    components: {
        NavigationBar,
    },

// Data ////////////////////////////////////////////////////////////////////////////

    data() {

        return {

            // Rozměry plátna
            canvasWidth: 869,
            canvasHeight: 502,

            // Enum společností [České dráhy, RegioJet, Leo Express]
            companies: Companies,

            // Enum aktivní link [routeSearch, trainMap]
            activeLink: ActiveLink,

            // 
            connectionNames: [] as Array<ConnectionName>,

            // Začátek / konec tras
            startRoutes: [] as Array<Route>,
            endRoutes: [] as Array<Route>,

            // Souřadnice kontrolních bodů
            checkpoints: [] as Array<Checkpoint>,
        }
    },

// Mounted /////////////////////////////////////////////////////////////////////////

    mounted() {

        // Nastavení mapy vlaků
        this.setTrainMap();

        // Obnovení mapy
        setInterval(() => {

            // Nastavení mapy vlaků
            this.setTrainMap();

        }, 2 * 60 * 1000);  // 2 minuty
    },

// Methods //////////////////////////////////////////////////////////////////////////

    methods: {


        // Formát času
        timeFormat: TimeFormat,


        /**
         * Nastavení mapy vlaků
         */
        setTrainMap(): void {

            // Přesměrování na přihlášení, při vypršení tokenu
            if (!VueCookieNext.isCookieAvailable("jwt")) {

                this.$router.push({
                    name: "LoginForm",
                    path: "/prihlaseni",
                });
            }

            // Vymazání předchozích dat
            this.connectionNames.length = 0;
            this.startRoutes.length = 0;
            this.endRoutes.length = 0;
            this.checkpoints.length = 0;

            // Získání dat pro mapu vlaků
            axios.get("http://localhost:8080/api/map", {

                headers: {
                    "Authorization": "Bearer " + VueCookieNext.getCookie("jwt")
                }

            }).then((responese) => {

                for (let i = 0; i < responese.data.length; i++) {

                    this.connectionNames.push(responese.data[i].connectionName);

                    this.startRoutes.push(responese.data[i].startRoute);
                    this.endRoutes.push(responese.data[i].endRoute);

                    this.checkpoints.push(responese.data[i].checkpoint);
                }

                // Vykreslení kontrolních bodů na mapě
                this.drawCheckpoints();

            }).catch(error => console.log(error));
        },


        /**
         * Vykreslení kontrolních bodů na mapě
         */
        drawCheckpoints(): void {

            const canvas = this.$refs.trainCanvas as HTMLCanvasElement;
            const context = canvas.getContext("2d") as CanvasRenderingContext2D;

            // Rozměry plátna
            canvas.height = this.canvasHeight;
            canvas.width = this.canvasWidth;

            for (let i = 0; i < this.checkpoints.length; i++) {

                const trainMark = new Image();

                switch (this.connectionNames[i].companyName.name) {

                    case this.companies.ceskeDrahy:
                        trainMark.src = TrainMarkCeskeDrahy;
                        break;

                    case this.companies.leoExpress:
                        trainMark.src = TrainMarkLeoExpress;
                        break;

                    case this.companies.regioJet:
                        trainMark.src = TrainMarkRegioJet;
                        break;
                }

                trainMark.onload = () => {

                    // Vykreslení značky
                    context.drawImage(trainMark,this.checkpoints[i].positionX - 10, 
                        this.checkpoints[i].positionY - 10, 20, 20);
                };
            }
        },


        /**
         * Vykreslení názvu vlaku
         * 
         * @param checkpoint - Kontrolní bod
         * @param trainName - Název vlaku
         */
        drawTrainName(checkpoint: Checkpoint, trainName: string): void {

            const canvas = this.$refs.labelCanvas as HTMLCanvasElement;
            const context = canvas.getContext("2d") as CanvasRenderingContext2D;

            // Rozměry plátna
            canvas.height = this.canvasHeight;
            canvas.width = this.canvasWidth;

            const height = 20;
            const margin = 4;
            const border = 2;
            const shift = 38;

            // Nastavení písma
            context.font = height + "px Avenir, Helvetica, Arial, sans-serif";
            context.textBaseline = "top";
            context.fillStyle = "white";

            const width = context.measureText(trainName).width;

            // Vnejší čtverec
            context.fillStyle = "#41B883";
            context.fillRect(checkpoint.positionX - (width / 2) - (margin + border), 
                             checkpoint.positionY - shift - (margin + border), 
                             width + 2 * (margin + border), 
                             height + 2 * (margin + border));

            // Vnitřní čtverec
            context.fillStyle = "#EEE";
            context.fillRect(checkpoint.positionX - (width / 2) - margin, 
                             checkpoint.positionY - shift - margin, 
                             width + 2 * margin, 
                             height + 2 * margin);

            // Text
            context.fillStyle = "#000000";
            context.fillText(trainName, checkpoint.positionX - (width / 2), checkpoint.positionY - shift);
        },


        /**
         * Vymazání plátna
         */
        clearTrainName(): void {

            const canvas = this.$refs.labelCanvas as HTMLCanvasElement;
            const context = canvas.getContext("2d") as CanvasRenderingContext2D;

            context.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
        }
    }
})
</script>

<style scoped src="@/components/train-map/train-map.css" />