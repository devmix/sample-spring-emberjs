import Component from '@ember/component';

export default Component.extend({
  tagName: 'a',
  classNames: ['e-tabsheet-tabs-tab', 'item'],
  classNameBindings: ['active:active'],

  observeId: Ember.observer('id', function () {
    const content = this.get('id');
    if (content) {
      content.set('active', this.get('active'));
    }
  }),

  didInsertElement() {
    this._super(...arguments);
    const tabs = this.get('tabs');
    if (tabs) {
      tabs.bindTab(this, true);
    }
  },

  willDestroyElement() {
    this._super(...arguments);
    const tabs = this.get('tabs');
    if (tabs) {
      tabs.bindTab(this, false);
    }
  },

  click(e) {
    this.get('tabs').active(this);
  }
});
