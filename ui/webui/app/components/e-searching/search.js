import Ember from 'ember';

export default Ember.Component.extend({

  classNames: ['e-searching-search'],

  searchText: undefined,
  searchedText: undefined,

  init() {
    this._super(...arguments);
    const search = this.get('target.search');
    if (search) {
      this.searchText = search;
      this.searchedText = search;
    }
  },

  notHasSearchText: Ember.computed('searchText', function () {
    return !this.get('searchText');
  }),

  doSearch() {
    const search = this.get('searchText');
    this.setProperties({
      'target.search': search,
      'searchedText': search
    });
  },

  actions: {
    search() {
      this.doSearch();
    },

    reset() {
      this.setProperties({
        'searchText': '',
        'searchedText': '',
        'target.search': ''
      });
    },

    searchKeyPress(e) {
      if (e.keyCode === 13 && this.get('search') !== this.get('searchText')) {
        this.doSearch();
      }
    }
  }
});
