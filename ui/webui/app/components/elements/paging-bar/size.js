import Ember from 'ember';

const { computed } = Ember;

export default Ember.Component.extend({

  value: 20, // current page size
  variants: '20 40 80', // selectable variants of page size

  tagName: '',

  variantAsList: computed('variants', 'value', function() {
    const variants = this.get('variants');
    if (!variants) {
      return [];
    }

    const value = this.get('value'), result = [];

    variants.split(' ').forEach((variant) => {
      result.push(Ember.Object.create({
        value: variant,
        checked: variant == value
      }));
    });

    return result;
  }),

  actions: {
    select(variant) {
      this.set('value', variant);
    }
  }
});
