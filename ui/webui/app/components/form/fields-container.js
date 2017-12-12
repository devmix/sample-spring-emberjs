import Ember from 'ember';

export default Ember.Component.extend({

  classNames: ['component-form-fields-container'],

  fields: {},
  valid: true,
  errors: [],

  commit() {
    if (this.validate()) {
      return this.get('model').save();
    }
    return Promise.reject();
  },

  didRender() {
    this._super(...arguments);
    const action = this.get('register');
    if (action) {
      this.sendAction('register', this)
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const action = this.get('unregister');
    if (action) {
      this.sendAction('unregister', this)
    }
  },

  invalid: Ember.computed('valid', function () {
      return !this.get('valid');
  }),

  validationCustom(errors) {
    return true;
  },

  validationReset() {
    this.set('valid', true);
    this.set('errors', []);
  },

  /**
   * @public
   * @returns {boolean}
   */
  validate() {
    let valid = true, errors = [], fields = this.get('fields');

    for (let id in fields) {
      if (fields.hasOwnProperty(id)) {
        valid &= fields[id].validate();
      }
    }

    valid &= this.validationCustom(errors);

    this.set('errors', errors);
    this.set('valid', valid);

    return valid;
  },

  cancel(cancelledCallback) {
    // TODO SG confirm dialog
    this.get('model').rollbackAttributes();
    cancelledCallback && cancelledCallback();
  },

  actions: {
    registerField(field) {
      this.get('fields')[field.get('elementId')] = field;
    },

    unregisterField(field) {
      delete this.get('fields')[field.get('elementId')];
    }
  }
});
