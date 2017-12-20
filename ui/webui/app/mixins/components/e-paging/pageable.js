import Ember from 'ember';

export default Ember.Mixin.create({

  pages: 0, // total amount of pages
  page: 0, // current page
  pageSize: 20, // current page size

  onRefresh() {
    // override me
  },

  updatePagingFromQueryResponse(response) {
    const meta = response.get('meta') || {}, page = meta.page;
    if (page) {
      this.setProperties({
        pages: Math.ceil(page.total / page.size),
        page: page.page,
        pageSize: page.size
      });
    }
  },

  watchPaging(promise) {
    promise.then((response) => this.updatePagingFromQueryResponse(response));
    return promise;
  }

});
