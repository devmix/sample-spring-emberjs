import Ember from 'ember';
import AutoRegisterMixin from 'webui/mixins/components/autoregister'

//noinspection JSUnusedLocalSymbols
export default Ember.Component.extend(AutoRegisterMixin, /*Validation,*/ {

  classNames: ['e-form-field', 'ui small field'],
  classNameBindings: ['invalid:error'],

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

  validate(allErrors = []) {
    let valid = true, errors = [], value = this.get('currentValue');
    if (this.get('required') && !value) {
      valid = false;
      const error = 'The value of field `' + this.get('label') + '` is required';
      errors.push(error);
      allErrors.push(error);
    }

    valid &= this.validationCustom(errors);

    this.setProperties({errors, valid});

    return valid;
  }
});
