export function initialize(application) {
  let Logger = {
    log(message) {
      console.log(message);
    }
  };

  application.register('logger:main', Logger, {
    instantiate: false,
    singleton: true
  });

  application.inject('route', 'logger', 'logger:main');
  application.inject('adapter', 'logger', 'logger:main');
}

export default {
  name: 'logger',
  initialize: initialize
};
