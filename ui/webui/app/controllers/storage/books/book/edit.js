import Ember from 'ember';

export default Ember.Controller.extend({
  fields: undefined,

  actions: {
    registerFields(field) {
      this.set('fields', field);
    },

    unregisterFields(field) {
      this.set('fields', undefined);
    },

    commit() {
      this.get('fields').commit().then((model) => {
        this.transitionToRoute('storage.books.book');
      });
    },

    cancel() {
      this.get('fields').cancel(() => history.back());
    }
  }
});
