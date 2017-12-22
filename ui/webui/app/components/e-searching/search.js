import Ember from 'ember';

export default Ember.Component.extend({

  classNames: ['components-e-searching-search'],

  searchText: undefined,
  searchedText: undefined,

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
