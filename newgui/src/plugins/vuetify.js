import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import IconRobot from '../components/IconRobot'
import IconPlugin from '../components/IconPlugin'

Vue.use(Vuetify);

export default new Vuetify({
    icons: {
        values: {
          robot: {
            component: IconRobot,
          },
          plugin: {
            component: IconPlugin,
          },
        },
      },
});
