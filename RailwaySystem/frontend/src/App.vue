<template>
  <div id="nav">
    <router-link to="/">Search Form</router-link>
  </div>

  <router-view />
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  
})
</script>

<style>
body {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  background-color: #EEE;
  padding: 0;
  margin: 0;
}

#app {
  display: flex;
  background: url(assets/background.jpg);
  background-repeat: no-repeat;
  background-attachment: fixed;
  height: 100vh;
}
</style>
