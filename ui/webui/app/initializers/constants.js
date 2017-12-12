export function initialize(application) {
  let Constants = {
    models: {
      books: {
        book: 'books-book'
      }
    }
  };

  application.register('constants:main', Constants, {
    instantiate: false,
    singleton: true
  });

  application.inject('route', 'constants', 'constants:main');
}

export default {
  name: 'constants',
  initialize: initialize
};
