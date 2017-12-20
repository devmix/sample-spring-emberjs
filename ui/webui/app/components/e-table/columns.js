import Ember from 'ember';

const { computed } = Ember;

export default Ember.Component.extend({

  tagName: 'thead',

  list: [],

  count: computed('list', function() {
    return this.get('list').length;
  }),

  registerChild(child) {
    this.get('list')[child.get('index')] = child;
  },

  unregisterChild(child) {
    delete this.get('list')[child.get('index')];
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
