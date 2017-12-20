import Ember from 'ember';

export default Ember.Mixin.create({

  __styleAttributesObserver: Ember.observer('style.{$,display,width,height,padding,margin,flexGrow}', function(sender, key) {
    const style = this.get('style');

    if (!style) {
      return;
    }

    if (this.__styleAttributesNextUpdateForAll) {
      for (let attribute in style) {
        this.setStyleAttribute(attribute, style[attribute]);
      }
      this.__styleAttributesNextUpdateForAll = false;
    } else {
      const attribute = key.substr(6);
      this.setStyleAttribute(attribute, style[attribute]);
    }
  }),

  didInsertElement() {
    this._super(...arguments);
    this.__styleAttributesNextUpdateForAll = true;
    this.notifyPropertyChange('style.$');
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
