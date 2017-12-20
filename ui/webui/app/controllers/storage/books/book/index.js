import Ember from 'ember';
import DS from 'ember-data';
import SearchableMixin from '../../../../mixins/components/e-searching/searchable';
import PageableMixin from '../../../../mixins/components/e-paging/pageable';

const {computed, observer, run} = Ember;

export default Ember.Controller.extend(SearchableMixin, PageableMixin, {

  filter: {},

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
    return this.watchPaging(this.get('store').query('books-book', this.get('filter')));

//    const promise = this.get('store').query('books-book', this.get('filter'));
//    promise.then((books) => {
//      const meta = books.get('meta');
//      if (meta && meta.page) {
//        const page = meta.page, pages = Math.ceil(page.total / page.size);
//        if (this.get('pages') != pages) {
//          this.setProperties({
//            page: page.page >= pages && pages > 0 ? pages - 1 : page.page,
//            size: page.size,
//            pages: pages
//          });
//        }
//      }
//    });
//    return promise;
  }),

  filter: computed('search','page','pageSize', function() {
    return {
      page: this.get('page'),
      size: this.get('pageSize'),
      search: this.get('search')
    }
  }),

  onRefresh() {
    this.notifyPropertyChange('filter');
  },

  actions: {
    create() {
      this.transitionToRoute('storage.books.book.new');
    }
  }

});
