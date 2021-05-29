<template>
<div class="navigationContainer">

    <!-- Logo -->
    <img src="@/assets/logo.png">
    <router-link to="/" />

    <div class="content">
        
        <!-- Vyhledání trasy -->
        <div>
            <router-link :class="(isRouteSearch ? 'linkActive' : 'linkInactive')" to="/">
                Vyhledání trasy
            </router-link>
        </div>
        
        <!-- Mapa vlaků -->
        <div>
            <router-link :class="(isTrainMap ? 'linkActive' : 'linkInactive')" to="/prihlaseni">
                Mapa vlaků
            </router-link>
        </div>
    </div>
</div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { ActiveLink } from "@/enums/activeLink";

export default defineComponent({

    name: "NavigationBar",

    props: ["linkSelection"],

// Data /////////////////////////////////////////////////////////////////////////////

    data() {
        return {

            // Enum aktivní link [routeSearch, trainMap]
            activeLink: ActiveLink,

            // Zobrazené odkazy
            isRouteSearch: false,
            isTrainMap: false,
        }
    },

// Mounted //////////////////////////////////////////////////////////////////////////

    mounted() {

        // Zvýraznění odkazu
        switch (this.linkSelection) {

            case ActiveLink.routeSearch: 
                this.isRouteSearch = true
                break;

            case ActiveLink.trainMap: 
                this.isTrainMap = true;
                break;
        }
    }
})
</script>

<style src="@/components/navigation-bar/navigation-bar.css" />