import Ember from 'ember';

const Button = Ember.Object.extend({
  text: undefined,
  action: undefined,
  icon: undefined
});

export default Ember.Component.extend({

  classNames: ['e-paging-controls'],

  buttons: [],

  buttonsConfig: Ember.computed('buttonsConfig', {
    set(key, newValue) {
      const result = [];
      if (newValue && newValue.length > 0) {
        newValue.forEach((item) => {
          const button = new Button({
            text: item.text,
            action: item.action,
            hint: item.hint,
            icon: item.icon
          });
          result.push(button);
        });
      }
      this.set('buttons', result);
    }
  }),

  actions: {
    invoke() {
      const target = this.get('target');
      if (target && target.invokeAction) {
        target.invokeAction(...arguments);
      }
    }
  }
});
