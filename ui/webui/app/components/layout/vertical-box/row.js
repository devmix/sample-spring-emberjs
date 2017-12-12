import Ember from 'ember';
import MixinComponentBase from '../../../mixins/components/base';

const { computed, observer } = Ember;

export default Ember.Component.extend(MixinComponentBase, {

  classNames: ['layout-row'],

  styleObserver: observer('grow', 'overflow', 'padding', function(sender, key) {
    const value = this.get(key);
    if (key == 'grow') {
      key = 'flexGrow';
    }
    this.setStyleAttribute(key, value);
  }),

  didRender() {
    this._super(...arguments);
    this.notifyPropertyChange('grow');
  }

});
