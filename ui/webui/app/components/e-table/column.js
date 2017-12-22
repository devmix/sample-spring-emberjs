import Ember from 'ember';

const {computed} = Ember;

export default Ember.Component.extend({

  tagName: 'th',
  attributeBindings: ['width'],

  index: undefined,

  config: computed('config', {
    set(key, newValue) {
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

  sortIcon: computed('direction', function () {
    const direction = this.get('direction');
    return direction === 'asc' ? 'sort-by-attributes'
      : (direction === 'desc' ? 'sort-by-attributes-alt' : undefined) || 'sort';
  }),

  applySortConfig(result) {
    if (this.sortable && this.direction) {
      result[this.id] = this.direction;
      return true;
    }
  },

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
  },

  actions: {
    onSortClick() {
      const oldDir = this.get('direction');
      this.set('direction', oldDir === 'asc' ? 'desc' : (oldDir === 'desc' ? undefined : 'asc'));
    }
  }
});
