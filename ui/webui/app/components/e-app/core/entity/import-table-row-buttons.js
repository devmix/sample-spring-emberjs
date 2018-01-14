import Component from '@ember/component';

export default Component.extend({

  isFinished: Ember.computed('cell.row.model.state', function () {
    return this.get('cell.row.model.state') === 'FINISHED';
  }),

  actions: {}
});
