import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import IconRobot from '../components/icons/IconRobot'
import IconPlugin from '../components/icons/IconPlugin'
import IconBell from '../components/icons/IconBell'

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
          bell: {
            component: IconBell
          }
        },
      },
});
