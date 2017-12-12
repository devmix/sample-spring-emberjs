import Ember from 'ember';

export default Ember.Controller.extend({
classNames: ['account-view'],

  actions: {
    onFindAll() {
      this.store.query('books-book', {
        page: 0,
        size: 10
      }).then((books) => {
        this.record = books.get('firstObject');
      });
    },
    onCreate() {
      this.store.createRecord('books-book', {title: 'test'}).save();
    },
    onRead() {
      this.store.findRecord('books-book', this.record.get('id')).then((book) => {
        console.log(book.get('id'));
        console.log(book.get('title'));
        console.log(book.get('authors').get('firstObject').get('id'));
      });
    },
    onUpdate() {
      const author = this.store.createRecord('books-author', {
//        id: new Date().getTime(),
        firstName: '123'
      });
      this.record.set('title', new Date().getTime() + '');
//      this.record.set('authors', [author]);
      this.record.save();
    },
    onDelete() {
      this.record.destroyRecord();
    }
  }
});
