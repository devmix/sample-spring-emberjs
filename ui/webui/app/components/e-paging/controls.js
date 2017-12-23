import Ember from 'ember';

export default Ember.Component.extend({
  classNames: ['e-paging-controls'],

  actions: {
    invoke() {
      const target = this.get('target');
      if (target && target.invokeAction) {
        target.invokeAction(...arguments);
      }
    }
  }
});
