import Component from '@ember/component';

export default Component.extend({
  tagName: '',

  columns: [
    {
      title: 'Entities', id: 'entities'
    },
    {
      title: 'State', id: 'state', sortable: true, width: '100px', renderer: function (v) {
        return Ember.String.capitalize((v || '').toLowerCase());
      }
    },
    {
      title: 'Start Date', id: 'startDate', sortable: true, direction: 'desc', width: '160px', renderer: function (v) {
        return moment(v).format('YYYY/MM/DD hh:mm:ss');
      }
    },
    {
      title: 'Finish Date', id: 'finishDate', sortable: true, width: '160px', renderer: function (v) {
        return moment(v).format('YYYY/MM/DD hh:mm:ss');
      }
    },
    // {
    //   title: '%', id: 'percent', sortable: true, width: '60px', renderer: function (v) {
    //     return v + '%';
    //   }
    // },
    {
      view: 'e-app/core/entity/import-table-row-buttons', width: '40px'
    }
  ],

  footerConfig: {
    buttons: [
      {icon: 'plus', hint: 'Upload', action: 'import'}
    ]
  },

  importService: Ember.inject.service('core/import'),

  actions: {
    onCustomAction(action) {
      if ('import' === action) {
        // TODO SG
      }
    }
  }
});
