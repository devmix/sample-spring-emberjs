import Ember from 'ember';

const { computed, observer } = Ember;

export default Ember.Mixin.create({

  __styleObserver: observer('width', 'height', function(sender, key) {
    this.setStyleAttribute(key, this.get(key));
  }),

  didInsertElement() {
    this._super(...arguments);
    this.notifyPropertyChange('width');
    this.notifyPropertyChange('height');
  },

  setStyleAttribute(name, value) {
    if (this.element) {
      this.element.style[name] = value;
    }
  },

  getStyleAttribute(name) {
    return this.element ? this.element.style[name] : undefined;
  }

});
