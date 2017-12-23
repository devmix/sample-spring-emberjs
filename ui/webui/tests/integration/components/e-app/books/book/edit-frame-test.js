import { moduleForComponent, test } from 'ember-qunit';
import hbs from 'htmlbars-inline-precompile';

moduleForComponent('e-app/books/book/edit-frame', 'Integration | Component | e app/books/book/edit frame', {
  integration: true
});

test('it renders', function(assert) {
  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{e-app/books/book/edit-frame}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#e-app/books/book/edit-frame}}
      template block text
    {{/e-app/books/book/edit-frame}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
