<template>
    <form @submit.prevent="" autocomplete="off">

        <table>
            <tr>
            <td>
                <div class="inputContainer">
                    <label for="from">Odkud:</label>

                    <input id="from" type="text" v-model="formInputs.from" 
                        @click="elementsProperties.focusFrom = true" 
                        @blur="elementsProperties.focusFrom = false"
                        @keyup="showOptions(formInputs.from, inputElement.from)">
                </div>

                <div v-show="elementsProperties.focusFrom" class="options">
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

                    <input id="to" type="text" v-model="formInputs.to" 
                    @focus="elementsProperties.focusTo = true" 
                    @blur="elementsProperties.focusTo = false" 
                    @keyup="showOptions(formInputs.to, inputElement.to)">
                </div>

                <div v-if="elementsProperties.focusTo" class="options">
                    <div v-for="station in filteredStationsTo" :key="station.id"
                        @mousedown="setStation(station.name, inputElement.to)">
                        {{station.name}}
                    </div>
                </div>
            </td>
            </tr>

            <tr>
                <td>
                    <div class="timeContainer">
                        <button @click="changeTime('-')">-</button>
                        <label>Odjezd:</label>
                        <div>{{ formInputs.departure }}</div>
                        <button @click="changeTime('+')">+</button>
                    </div>
                </td>
            </tr>

            <tr>
                <td>
                    <button>Vyhledat</button>
                </td>
            </tr>
        </table>

    </form>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { Station } from "../models/stations";
import { InputElement } from "../models/inputElement";
import axios from 'axios';

export default defineComponent({
    
    name: "SearchForm",

    data() {
        return {
            inputElement: InputElement,
            elementsProperties: {
                focusFrom: false,
                focusTo: false,
            },
            formInputs: {
                from: "",
                to: "",
                departure: new Date().getHours() + ":00",
            },
            stations: [] as Array<Station>,
            filteredStationsFrom: [] as Array<Station>,
            filteredStationsTo: [] as Array<Station>,
        }
    },

    mounted() {

        axios.get("http://localhost:8080/api/stations")
            .then((response) => {

                this.stations = response.data;

                this.filteredStationsFrom = this.stations;
                this.filteredStationsTo = this.stations;

            }).catch(error => console.log(error));
    },

    methods: {
        changeTime(mark: string): void {
            
            let hour: number = parseInt(this.formInputs.departure.split(":")[0]);

            if (mark === "+") {

                if (hour < 23) {
                    hour++;
                } else hour = 0;

            } else if (mark === "-") {

                if (hour > 0) {
                    hour--;
                } else hour = 23;
            }

            this.formInputs.departure = hour + ":00";
        },

        showOptions(value: string, inputSelect: InputElement) {

            switch (InputElement[inputSelect]) {

                case InputElement.from:
                    this.filteredStationsFrom = this.stations.filter(station => station.name.includes(value));
                    break;

                case InputElement.to:
                    this.filteredStationsTo = this.stations.filter(station => station.name.includes(value));
                    break;
            }
        },

        setStation(stationName: string, inputElement: InputElement) {
            
            switch (InputElement[inputElement]) {

                case InputElement.from:
                    this.formInputs.from = stationName;
                    this.elementsProperties.focusFrom = false;
                    break;

                case InputElement.to:
                    this.formInputs.to = stationName;
                    this.elementsProperties.focusTo = false;
                    break;
            }
        }
    },

    computed: {
        timeFormat(): string {
            return new Date().getHours() + ":00";
        }
    },
});
</script>

<style scoped>
form {
    width: 1200px;
    height: 500px;
    margin: 100px auto;
    background: #EEE;
    border: 2px solid #c2c2c2;
    border-radius: 5px;
}

table {
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

.timeContainer {
    display: inline-block; 
    margin: 50px 0px 0px 0px;
    border: 2px solid #41B883;
    border-radius: 5px;
}

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

button {
    color: #EEE;
    font-size: 1.2em;
    width: 604px;
    margin: 50px 0px 0px 0px;
    padding: 10px 20px;
    background: #34495E;
    transition: background 0.5s ease-in-out;
    border-radius: 5px;
    border: none;
}

button:hover {
    cursor: pointer;
    background: #41B883;
}

button:focus {
    outline: none;
}
</style>