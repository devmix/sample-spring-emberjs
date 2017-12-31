import Service from '@ember/service';

export default Service.extend({

  entityTableButtons: [
    {icon: 'plus', hint: 'Create new...', action: 'e-table-create-new-item'},
    {icon: 'upload', hint: 'Import...', action: 'e-table-import-items'},
    {icon: 'download', hint: 'Export...', action: 'e-table-export-items'}
  ]

});
