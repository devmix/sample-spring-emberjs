import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-app/commons/table/cell-entity-buttons', 'Integration | Component | e app/commons/table/cell entity buttons', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-app/commons/table/cell-entity-buttons}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-app/commons/table/cell-entity-buttons}}
      template block text
    {{/e-app/commons/table/cell-entity-buttons}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
