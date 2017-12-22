import Ember from 'ember';

//noinspection JSUnusedLocalSymbols
export default Ember.Component.extend(/*Validation,*/ {

  classNames: ['form-group', 'form-group-sm'],
  classNameBindings: ['invalid:has-error'],

  buffered: false,
  valid: true,
  required: false,
  errors: [],

  currentValue: Ember.computed('value', {
    get(key) {
      return this.get('value');
    },
    set(key, value) {
      if (!this.get('buffered')) {
        this.set('value', value);
      }
      return value;
    }
  }),

  didInsertElement() {
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

  validate() {
    let valid = true, errors = [], value = this.get('currentValue');
    if (this.get('required') && !value) {
      valid = false;
      errors.push('The value of field is required');
    }

    valid &= this.validationCustom(errors);

    this.set('errors', errors);
    this.set('valid', valid);

    return valid;
  }
});
