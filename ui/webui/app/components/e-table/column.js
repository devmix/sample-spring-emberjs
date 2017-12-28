import Ember from 'ember';
import AttributeMixin from 'webui/mixins/components/e-table/attribute';

const {computed} = Ember;

export default Ember.Component.extend(AttributeMixin, {

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
      this.sortToggle();
    }
  }
});
