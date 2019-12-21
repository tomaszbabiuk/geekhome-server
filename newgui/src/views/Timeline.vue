<template>
  <div class="timeline">
    <h1 class="subheading grey--text">Timeline</h1>

    <v-timeline>
      <v-timeline-item v-for="(event, i) in events" :key="i" :color="event.color" small>
        <template v-slot:opposite>
          <span :class="`headline font-weight-bold ${event.color}--text`" v-text="event.event"></span>
        </template>
        <v-card class="elevation-2">
          <v-card-title class="headline">
            {{ event.actor }}
            </v-card-title>
          <v-card-text>{{ event.when }}</v-card-text>
        </v-card>
      </v-timeline-item>
    </v-timeline>
  </div>
</template>

<script>
export default {
  data: () => ({
    events: [
      {
        actor: "Pump 1",
        color: "cyan",
        event: "ON",
        when: "now",
        svgIconRef: require("../assets/ic_robot.svg")
      },
      {
        actor: "Furnace",
        color: "green",
        event: "OFF",
        when: "2 mins ago",
        svgIconRef: require("../assets/ic_robot.svg")
      },
      {
        actor: "Pump 2",
        color: "pink",
        event: "ON",
        when: "25 mins ago",
        svgIconRef: require("../assets/ic_robot.svg")
      },
      {
        actor: "Light 3",
        color: "amber",
        event: "Heating",
        when: "1h ago",
        svgIconRef: require("../assets/ic_robot.svg")
      },
      {
        actor: "Light 2",
        color: "orange",
        event: "Cooling",
        when: "3h ago"
      }
    ]
  })
};
</script>