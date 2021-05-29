<template>

<!-- Navigační lišta -->
<NavigationBar :linkSelection="activeLink.trainMap" />

<form @submit.prevent="onSubmit" autocomplete="off">

    <div class="content">

        <div class="loginLabel">Přihlašovací formulář</div>

        <!-- Chybová zpráva -->
        <div>
            <div v-if="isError" class="errorMessage">

                <div>{{ errorMessage }}</div>
            </div>
        </div>

        <!-- ID uživatele -->
        <div>
            <div class="inputContainer">
                
                <label for="id">ID:</label>
                <input id="id" type="text" v-model="formInputs.id">
            </div>
        </div>

        <!-- Heslo -->
        <div>
            <div class="inputContainer">

                <label for="password">Heslo:</label>
                <input id="password" type="password" v-model="formInputs.password">
            </div>
        </div>

        <!-- Tlačítko pro přihlášení -->
        <div>
            <button :disabled="formInputs.id === '' || formInputs.password === ''"
                :class="(formInputs.id === '' || formInputs.password === '') ? 'disabled' : ''">

                Přihlásit
            </button>
        </div>
    </div>
</form>

</template>

<script lang="ts">
import { defineComponent } from 'vue'
import axios from "axios";
import { VueCookieNext } from 'vue-cookie-next'
import NavigationBar from "@/components/navigation-bar/NavigationBar.vue";
import { ActiveLink } from "@/enums/activeLink";

export default defineComponent({
    
    name: "LoginForm",

    components: {
        NavigationBar,
    },

// Data /////////////////////////////////////////////////////////////////////////////////////////////

    data() {

        return {

            // Enum aktivní link [routeSearch, trainMap]
            activeLink: ActiveLink,

            // Data formuláře
            formInputs: {
                id: "",
                password: ""
            },

            // Chybová zpráva
            isError: false,
            errorMessage: "Nesprávné přihlašovací údaje",
        }
    },

// Methods /////////////////////////////////////////////////////////////////////////////////////////

    methods: {


        /**
         * Odeslání formuláře
         */
        onSubmit() {

            // Ověření uživatele
            axios.post("http://localhost:8080/api/authenticate", {

                username: this.formInputs.id,
                password: this.formInputs.password
                
            }).then((response) => {

                this.isError = false;

                const token: string = response.data.token;
                const expireTime: string = response.data.expireTime;

                let isCookieExist: boolean = VueCookieNext.isCookieAvailable("jwt");

                // Smazání existujícího cookie
                if (isCookieExist) {
                    VueCookieNext.removeCookie("jwt");
                }

                // Vytvoření nového cookie
                VueCookieNext.setCookie("jwt", token, {expire: new Date(expireTime)});

                this.$router.push({
                    name: "TrainMap",
                    path: "/mapa",
                });

            }).catch(error => {

                this.isError = true;

                console.log(error)
            });          
        },
    }
})
</script>

<style scoped src="@/components/login-form/login-form.css" />