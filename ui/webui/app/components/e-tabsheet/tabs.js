import Component from '@ember/component';

const {guidFor} = Ember;

export default Component.extend({

  classNames: ['menu'],
  classNameBindings: ['right:right'],

  list: {},

  bindTab(tab, bind) {
    if (bind) {
      this.get('list')[guidFor(tab)] = tab;
    } else {
      delete this.get('list')[guidFor(tab)];
    }
  },

  active(tab) {
    if (tab.get('active')) {
      return;
    }

    const content = tab.get('id'), tabId = guidFor(tab), list = this.get('list');
    for (let id in list) {
      if (list.hasOwnProperty(id) && id !== tabId) {
        list[id].setProperties({active: false, 'id.active': false});
      }
    }

    tab.setProperties({active: true, 'id.active': true});
  }
});
