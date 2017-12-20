import Ember from 'ember';

const { computed, observer } = Ember;

export default Ember.Component.extend({

  tagName: 'th',
  attributeBindings: ['width'],

  index: undefined,

  config: computed('config', {
    set(key, newValue, oldValue) {
      this.setProperties({
        id: newValue.id,
        title: newValue.title,
        sortable: newValue.sortable,
        direction: newValue.direction,
        width: newValue.width,
        cellRenderer: newValue.renderer,
        cellView: newValue.view
      })
    }
  }),

  didInsertElement() {
    this._super(...arguments);
    const parent = this.get('parent');
    if (parent) {
      parent.registerChild(this)
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const parent = this.get('parent');
    if (parent) {
      parent.unregisterChild(this)
    }
  }
});
