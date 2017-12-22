import Ember from 'ember';

export default Ember.Component.extend({
  tagName: 'div',
  classNames: ['e-table-grid'],

  attributesConfig: Ember.computed({
    set(key, newValue, oldValue) {
      this.set('attributes', newValue);
    }
  })
});
