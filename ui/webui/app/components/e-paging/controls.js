import Ember from 'ember';

export default Ember.Component.extend({
  classNames: ['components-e-paging-controls'],

  actions: {
    invoke() {
      const target = this.get('target');
      if (target && target.invokeAction) {
        target.invokeAction(...arguments);
      }
    }
  }
});
