import Ember from 'ember';

export default Ember.Component.extend({

  tagName: 'div',
  classNames: ['e-table-table'],

  columns: undefined,

  registerColumnsComponent(columns) {
    this.set('columns', columns);
  },

  unregisterColumnsComponent() {
    this.set('columns', undefined);
  },

  didInsertElement() {
    this._super(...arguments);
    const parent = this.get('component');
    if (parent) {
      parent.registerTableComponent(this)
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const parent = this.get('component');
    if (parent) {
      parent.unregisterTableComponent(this)
    }
  }
});
