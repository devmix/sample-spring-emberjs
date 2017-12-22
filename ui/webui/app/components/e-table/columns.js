import Ember from 'ember';

const {computed} = Ember;

export default Ember.Component.extend({

  tagName: 'thead',

  list: [],

  count: computed('list', function () {
    return this.get('list').length;
  }),

  hasSorting: false,

  sortOrders: Ember.observer('list.@each.{direction}', function () {
    const result = {};
    let hasSorting = false;
    this.get('list').forEach((column) => {
      hasSorting |= column.applySortConfig(result);
    });

    if (this.hasSorting || hasSorting) {
      const component = this.get('table.component');
      if (component) {
        component.set('sort', result);
      }
      this.hasSorting = hasSorting;
    }
  }),

  registerChild(child) {
    this.get('list').pushObject(child);
  },

  unregisterChild(child) {
    this.get('list').removeObject(child);
  },

  didInsertElement() {
    this._super(...arguments);
    const parent = this.get('table');
    if (parent) {
      parent.registerColumnsComponent(this)
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const parent = this.get('table');
    if (parent) {
      parent.unregisterColumnsComponent(this)
    }
  }
});
