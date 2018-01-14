import Ember from 'ember';

export default Ember.Component.extend({
  tagName: '',
  actions: {
    onClick(action) {
      this.sendAction('onAction', action);
    }
  }
});
