import Ember from 'ember';

export default Ember.Mixin.create({

  pages: 0, // total amount of pages
  page: 0, // current page
  pageSize: 20, // current page size

  updatePagingFromQueryResponse(response) {
    const meta = response.get('meta') || {}, page = meta.page;
    if (page) {
      const newPages = Math.ceil(page.total / page.size);
      if (newPages !== this.get('pages')) {
        this.setProperties({
          pages: newPages,
          page: Math.min(page.page, newPages - 1),
          pageSize: page.size
        });
      }
    }
  },

  watchPaging(promise) {
    promise.then((response) => this.updatePagingFromQueryResponse(response));
    return promise;
  }

});
