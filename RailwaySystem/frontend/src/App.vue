<template>
    <!-- <div>
        <router-link to="/">Search Form</router-link>
    </div> -->

    <router-view />
</template>

<style>
body {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    background-color: #EEE;
    padding: 0;
    margin: 0;
}

#app {
    display: flex;
    flex-direction: column;
    background: url(assets/background.jpg);
    background-repeat: no-repeat;
    background-attachment: fixed;
    min-height: 100vh;
    height: 100%;
}

.navigation {
    width: 1200px;
    display: flex;
    justify-content: space-around;
    margin: 5px auto 20px auto;
    background: #EEE;
    border: 2px solid #c2c2c2;
    border-radius: 5px;
}

.header {
    background: #41B883;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    color: #EEE;
    padding: 9px 15px;
}
</style>
