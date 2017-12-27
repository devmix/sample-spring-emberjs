import Service from '@ember/service';

const DEFAULT_OPTIONS = {
  session: true
};

export default Service.extend({

  set(key, value, options = DEFAULT_OPTIONS) {
    const storage = options.global ? window.sessionStorage : window.localStorage;
    storage.setItem(key, value ? JSON.stringify(value) : undefined);
  },

  get(key, options = DEFAULT_OPTIONS) {
    const storage = options.global ? window.sessionStorage : window.localStorage,
      value = storage.getItem(key);
    if (value) {
      try {
        return JSON.parse(value);
      } catch (ignore) {
        // ignore
      }
    }
    return undefined;
  }

});
