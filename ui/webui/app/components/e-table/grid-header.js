import Component from '@ember/component';
import AttributeMixin from 'webui/mixins/components/e-table/attribute';

const Attribute = Ember.Object.extend(AttributeMixin);

export default Component.extend({

  tagName: 'div',
  classNames: ['e-table-grid-header', 'ui icon compact menu small'],

  attributes: [],
  hasSorting: false,

  attributesConfig: Ember.computed({
    set(key, newValue, oldValue) {
      const attributes = [];
      if (newValue && newValue.length > 0) {
        newValue.forEach((attribute) => {
          if (attribute.sortable) {
            attributes.push(Attribute.create({
              id: attribute.id,
              title: attribute.title,
              sortable: attribute.sortable
            }))
          }
        })
      }
      this.set('attributes', attributes);
    }
  }),

  observeSort: Ember.observer('attributes.@each.{direction}', function () {
    const result = {};
    let hasSorting = false;
    this.get('attributes').forEach((attribute) => {
      hasSorting |= attribute.sortApplyConfig(result);
    });

    if (this.hasSorting || hasSorting) {
      this.set('grid.component.sort', result);
      this.hasSorting = hasSorting;
    }
  }),

  actions: {
    onSortClick(attribute) {
      attribute.sortToggle();
    }
  }
});
