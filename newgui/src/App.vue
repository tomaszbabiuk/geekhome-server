<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>
        geek
        <span headline>HOME</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>mdi-heart</v-icon>
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title">geekHOME</v-list-item-title>
          <v-list-item-subtitle>Automation Studio</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item link v-for="item in navigationItems" :key="item.title" :to="item.route">
          <v-list-item-action>
            <v-img :src="item.svgIconRef" contain height="25" width="25"></v-img>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title class="grey--text" style="fill: red">{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-content class="mx-4 mb-4">
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: "App",

  components: {},

  data: () => ({
    drawer: false,
    navigationItems: [
      { title: "Inbox", route: "/inbox", svgIconRef: require("./assets/ic_robot.svg") },
      { title: "Timeline", route: "/timeline", svgIconRef: require("./assets/ic_timeline.svg") },
      { title: "Alerts", route: "/alerts", svgIconRef: require("./assets/ic_bell.svg") },
      { title: "Control", route: "/control", svgIconRef: require("./assets/ic_button.svg") },
      { title: "House", route: "/house", svgIconRef: require("./assets/ic_house.svg") },
      { title: "Discover", route: "/discover", svgIconRef: require("./assets/ic_crosshair.svg") },
      { title: "Settings", route: "/settings", svgIconRef: require("./assets/ic_equalizer.svg") },
      { title: "Plugins", route: "/plugins", svgIconRef: require("./assets/ic_plugin.svg") }
    ]
  })
};
</script>