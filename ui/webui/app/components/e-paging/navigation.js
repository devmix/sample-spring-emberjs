import Ember from 'ember';

const { computed } = Ember;

export default Ember.Component.extend({

  classNames: ['components-e-paging-navigation'],

  pageSizeVariants: '20 40 80', // selectable variants of page size

  noPrev: computed('target.page', function() {
    return this.get('target.page') == 0;
  }),

  noNext: computed('target.page', 'pages', function() {
    return this.get('target.page') >= this.get('target.pages') - 1;
  }),

  currentPage: computed('target.page', function() {
    return this.get('target.page') + 1;
  }),

  pages: computed('target.pages', function() {
    return this.get('target.pages');
  }),

  pageSize: computed('target.pageSize', function() {
    return this.get('target.pageSize');
  }),

  pageSizes: computed('pageSizeVariants', 'target.pageSize', function() {
    const variants = this.get('pageSizeVariants');
    if (!variants) {
      return [];
    }

    const value = this.get('target.pageSize'), result = [];

    variants.split(' ').forEach((variant) => {
      result.push(Ember.Object.create({
        value: variant,
        checked: variant == value
      }));
    });

    return result;
  }),

  actions: {
    prev() {
      if (!this.get('noPrev')) {
        this.set('target.page', this.get('target.page') - 1);
      }
    },

    next() {
      if (!this.get('noNext')) {
        this.set('target.page', this.get('target.page') + 1);
      }
    },

    selectPageSize(pageSize) {
      this.set('target.pageSize', pageSize);
    },

    refresh() {
      const owner = this.get('target');
      if (owner && owner.onRefresh) {
        owner.onRefresh();
      }
    }
  }

});
