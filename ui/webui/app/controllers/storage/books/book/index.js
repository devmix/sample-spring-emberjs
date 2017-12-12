import Ember from 'ember';
import DS from 'ember-data';

const {computed, observer, run} = Ember;

export default Ember.Controller.extend({

  filter: {},
  search: undefined,
  searchText: undefined,
  page: 0,
  size: 20,
  pages: 0,

  init() {
    this._super(...arguments);

  },

  list: computed('filter', function () {
//    return DS.PromiseArray.create({
//      promise: new Ember.RSVP.Promise((resolve, reject) => {
//         run.debounce({name: 'storage-books-book-index-query'}, () => {
//           resolve(this.get('store').query('books-book', this.get('filter')));
//         }, 1000)
//       })
//    });
    const promise = this.get('store').query('books-book', this.get('filter'));
    promise.then((books) => {
      const meta = books.get('meta');
      if (meta && meta.page) {
        const page = meta.page, pages = Math.ceil(page.total / page.size);
        if (this.get('pages') != pages) {
          this.setProperties({
            page: page.page >= pages && pages > 0 ? pages - 1 : page.page,
            size: page.size,
            pages: pages
          });
        }
      }
    });
    return promise;
  }),

  filter: computed('search','page','size', function() {
    return {
      page: this.get('page'),
      size: this.get('size'),
      search: this.get('search')
    }
  }),

  hasSearchText: computed('searchText', function() {
    return !this.get('searchText');
  }),

//  observeSearch: observer('searchText', function() {
//    run.debounce(this, this.notifySearchChanged, 1000);
//  }),

//  notifySearchChanged() {
//    this.set('search', this.get('searchText'));
//  },

  actions: {
    refresh() {
      this.notifyPropertyChange('filter');
    },
    search() {
      if (this.get('search') !== this.get('searchText')) {
        this.set('search', this.get('searchText'));
      }
    },
    reset() {
      this.setProperties({
        searchText: undefined,
        search: undefined
      });
    },
    searchKeyPress(e) {
      if (e.keyCode == 13 && this.get('search') !== this.get('searchText')) {
        this.set('search', this.get('searchText'));
      }
    },
    create() {
      this.transitionToRoute('storage.books.book.new');
    }
  }

});
