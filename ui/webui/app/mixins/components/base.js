import Ember from 'ember';

export default Ember.Mixin.create({

  __styleObserver: Ember.observer('width', 'height', function (sender, key) {
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
