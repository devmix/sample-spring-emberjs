import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'td',

  init() {
    this._super(...arguments);

    const column = this.get('column');
    if (column.get('cellView')) {
      return;
    }

    const id = column.get('id'), cellRenderer = column.get('cellRenderer');
    if (cellRenderer) {
      const model = this.get('row.model');
      this.value = Ember.computed('row.model.*', function () {
        return cellRenderer(this.get('column'), this.get('row'), model);
      }).readOnly();
    } else {
      this.value = Ember.computed('row.model.{' + id + '}', function () {
        return this.get('row.model').get(id);
      }).readOnly();
    }
  }
});
