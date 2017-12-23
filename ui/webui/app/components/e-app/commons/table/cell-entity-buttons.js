import Ember from 'ember';

export default Ember.Component.extend({

  classNames: ['e-app-commons-table-cell-entity-buttons', 'btn-group', 'btn-group-sm'],

  actions: {
    edit() {
      this.get('cell.row.component').invokeAction('cell-button-edit', this.get('cell'));
    },

    remove() {
      this.get('cell.row.component').invokeAction('cell-button-remove', this.get('cell'));
    }
  }
});
