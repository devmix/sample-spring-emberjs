import Ember from 'ember';

const { computed } = Ember;

export default Ember.Component.extend({

  current: 0, // current page
  total: 0, // total amount of pages

  tagName: '',

  noPrev: computed('current', function() {
    return this.get('current') == 0;
  }),

  noNext: computed('current', 'total', function() {
    return this.get('current') >= this.get('total') - 1;
  }),

  currentPage: computed('current', function() {
    return this.get('current') + 1;
  }),

  actions: {
    prev() {
      if (!this.get('noPrev')) {
        this.set('current', this.get('current') - 1);
      }
    },
    next() {
      if (!this.get('noNext')) {
        this.set('current', this.get('current') + 1);
      }
    }
  }

});
